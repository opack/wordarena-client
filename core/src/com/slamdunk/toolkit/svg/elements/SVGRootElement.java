package com.slamdunk.toolkit.svg.elements;

import java.util.ArrayList;

public class SVGRootElement extends SVGElement{
	
	ArrayList<SVGElement> defs;
	
	public SVGRootElement() {
		super(null, "svg");
	}

	public int width;
	public int height;
	public int format;
	
	public float min_x;
    public float min_y;
    public float max_x;
    public float max_y;

    public float scale;
    
    private boolean childIsDef;
	
	public void setChildIsDef(boolean childIsDef) {
		this.childIsDef = childIsDef;
	}
	
	public boolean isChildIsDef() {
		return childIsDef;
	}

	public long getLongData(){
		return width * height * format;
	}
	
	@Override
	public void addChild(SVGElement element) {
		if ( !childIsDef ){
			super.addChild(element);
		}else{
			if ( defs == null ){
				defs = new ArrayList<SVGElement>();
			}
			defs.add(element);
		}
	}
	
	@Override
	public void addAttribute(String attribute, float value){
		if ( "width".equals(attribute) ){
			width  = (int)value;
		} else if ( "height".equals(attribute) ){
			height = (int)value;
		} else if ( "format".equals(attribute) ){
			format = (int)value;
		} else if ( "min_x".equals(attribute) ){
			min_x = value;
		} else if ( "max_x".equals(attribute) ){
			max_x = value;
		}else if ( "min_y".equals(attribute) ){
			min_y = value;
		} else if ( "max_y".equals(attribute) ){
			max_y = value;
		} else if ( "scale".equals(attribute) ){
			scale = value;
		}
	}
}
