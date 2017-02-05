/*
 */
package com.github.quikmod.quikcore.reflection;

import com.github.quikmod.quikcore.reflection.exceptions.UnknownQuikDomainException;
import com.github.quikmod.quikcore.util.RegistrationConflictException;
import com.github.quikmod.quikcore.util.ReflectionStreams;
import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author Ryan
 */
public final class QuikDomains {

	private final Set<String> domains;
	private final ConcurrentInvertedRadixTree<String> domainTree;

	public QuikDomains() {
		// Initialize
		this.domains = new HashSet<>();
		this.domainTree = new ConcurrentInvertedRadixTree<>(new DefaultCharArrayNodeFactory());
		
		// Manually add QuikCore
		this.domains.add("quikcore");
		this.domainTree.put("com.github.quikmod.quikcore", "quikcore");
	}

	public String resolveDomain(Package pack) throws UnknownQuikDomainException {
		return resolveDomain(pack.getName());
	}
	
	public String resolveDomain(Class<?> clazz) throws UnknownQuikDomainException {
		return resolveDomain(clazz.getName());
	}
	
	public String resolveDomain(Member m) throws UnknownQuikDomainException {
		return resolveDomain(m.getDeclaringClass().getName() + "." + m.getName());
	}

	public String resolveDomain(String path) throws UnknownQuikDomainException {
		// Format the path.
		path = formatPath(path);

		// Fetch the closest result.
		final String domain = domainTree.getValueForLongestKeyPrefixing(path);

		// Determine if unknown domain.
		if (domain == null) {
			throw new UnknownQuikDomainException(path);
		}

		// Else Return Domain
		return domain;
	}
	
	public Optional<String> attemptResolveDomain(String path) {
		return Optional.ofNullable(domainTree.getValueForLongestKeyPrefixing(formatPath(path)));
	}
	
	public Optional<String> attemptResolveDomain(Member member) {
		return attemptResolveDomain(member.getDeclaringClass().getName() + "." + member.getName());
	}
	
	public Optional<String> attemptResolveDomain(Class<?> clazz) {
		return attemptResolveDomain(clazz.getName());
	}
	
	public Optional<String> attemptResolveDomain(Package pack) {
		return attemptResolveDomain(pack.getName());
	}
	
	protected void registerDomain(Package ctx) throws RegistrationConflictException {
		if (ctx.isAnnotationPresent(QuikDomain.class)) {
			registerDomain(ctx.getAnnotation(QuikDomain.class).value(), ctx.getName());
		}
	}

	protected void registerDomain(Class ctx) throws RegistrationConflictException {
		// Register local domain.
		if (ctx.isAnnotationPresent(QuikDomain.class)) {
			final String domain = ((QuikDomain) ctx.getAnnotation(ctx)).value();
			registerDomain(domain, ctx.getName());
		}

		// Register Subdomains
		ReflectionStreams.streamMembers(ctx).forEach(this::registerDomain);
	}

	protected <T extends AccessibleObject & Member> void registerDomain(T ctx) throws RegistrationConflictException {
		if (ctx.isAnnotationPresent(QuikDomain.class)) {
			registerDomain(ctx.getAnnotation(QuikDomain.class).value(), ctx.getDeclaringClass().getName() + "." + ctx.getName());
		}
	}

	private void registerDomain(String id, String path) throws RegistrationConflictException {
		// Format Path
		path = formatPath(path);

		// Format Id
		id = formatId(id);

		// Fetch Previous
		final String previous = this.domainTree.putIfAbsent(path, id);

		// Determine if Collison
		if (id.equals(previous)) {
			// Same registration. Twice.
			// Why would you do such a thing?
			return;
		} else if (previous != null) {
			throw new RegistrationConflictException(this, id, path, previous, "QuikDomain registration conflict!");
		}

		// Add Domain to Known Domains. Fin.
		this.domains.add(id);
	}

	private static String formatPath(String path) {
		path = path.trim().toLowerCase();
		path = path.endsWith(".") ? (path) : (path + '.');
		return path;
	}

	private static String formatId(String id) {
		return id.trim().toLowerCase();
	}

	@Override
	public String toString() {
		return PrettyPrinter.prettyPrint(this.domainTree);
	}

}
