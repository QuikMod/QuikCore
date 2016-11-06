/*
 */
package com.github.quickmod.quiklib.json;

import com.github.quickmod.quiklib.core.QuikCore;

/**
 *
 */
public class QuikStack {
    
    protected final String item;
    
    protected final int meta;
    
    protected final String tags;
    
    protected final boolean ignoreMeta;
    
    protected final boolean ignoreTags;
    
    protected final boolean useOreDict;

    public QuikStack() {
        this.item = "minecraft:dirt";
        this.meta = 0;
        this.tags = "";
        this.ignoreMeta = false;
        this.ignoreTags = false;
        this.useOreDict = false;
    }

    public QuikStack(String item, int meta, String tags, boolean ignoreMeta, boolean ignoreTags, boolean useOreDict) {
        this.item = item;
        this.meta = meta;
        this.tags = tags;
        this.ignoreMeta = ignoreMeta;
        this.ignoreTags = ignoreTags;
        this.useOreDict = useOreDict;
    }

    public String getItem() {
        return item;
    }

    public int getMeta() {
        return meta;
    }

    public String getTags() {
        return tags;
    }

    public boolean isIgnoreMeta() {
        return ignoreMeta;
    }

    public boolean isIgnoreTags() {
        return ignoreTags;
    }

    public boolean isUseOreDict() {
        return useOreDict;
    }
    
    public Object toStack() {
        return this.toStack(1);
    }
    
    public Object toStack(int amount) {
        return QuikCore.getConverter().toStack(item, meta, amount, tags, ignoreMeta, ignoreTags, useOreDict);
    }
    
    public boolean validate() {
        return QuikCore.getValidator().isValidItem(item);
    }
    
    @Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\nStack:");
		sb.append("\n\t- Item: ").append(item);
		sb.append("\n\t- Meta: ").append(meta);
		sb.append("\n\t- Tags: ").append(tags);
		sb.append("\n\t- IgnoreMeta: ").append(ignoreMeta);
		sb.append("\n\t- IgnoreTags: ").append(ignoreTags);
		sb.append("\n\t- UseOreDict: ").append(useOreDict);
		return sb.toString();
	}
    
}
