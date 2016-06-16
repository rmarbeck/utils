package fr.watchnext.utils.views;


import java.util.ArrayList;
import java.util.List;

import play.api.mvc.Call;

/**
 * Definition of Model in order to be displayed
 */
public class SimpleNavbar {
	public class LinksGroup {
		public String labelKey;
		public List<Link> links;
		
		protected LinksGroup(String labelKey) {
			this.labelKey = labelKey;
			this.links = new ArrayList<SimpleNavbar.Link>();
		}
		
		public LinksGroup add(Link newLink) {
			links.add(newLink);
			return this;
		}
		
		public LinksGroup add(String labelKey, String destination, boolean hideSmall, Integer index) {
			return add(new Link(labelKey, destination, hideSmall, index));
		}
		
		public LinksGroup add(String labelKey, Call destination, boolean hideSmall, Integer index) {
			return add(new Link(labelKey, destination, hideSmall, index));
		}
		
		public String getLabelKey() {
			return labelKey;
		}
		
	}
	public class Link {
		public String labelKey;
		public String destination;
		public Integer index;
		public boolean hideSmall;

		protected Link(String labelKey, String destination, boolean hideSmall, Integer index) {
			this.labelKey = labelKey;
			this.destination = destination;
			this.hideSmall = hideSmall;
			this.index = index;
		}
		
		protected Link(String labelKey, Call destination, boolean hideSmall, Integer index) {
			this.labelKey = labelKey;
			this.destination = destination.path();
			this.hideSmall = hideSmall;
			this.index = index;
		}
		
		protected Link(String labelKey, String destination, boolean hideSmall) {
			this(labelKey, destination, hideSmall, null);
		}
		
		protected Link(String labelKey, String destination) {
			this(labelKey, destination, false);
		}
		
		public String getLabelKey() {
			return labelKey;
		}
		
		public String getDestination() {
			return destination;
		}
		
		public Integer getIndex() {
			return index;
		}
	}
	public String labelKeyBeginning;
	public Link brandLink;
	public List<LinksGroup> leftLinks;
	public List<LinksGroup> rightLinks;
	
	public SimpleNavbar(String labelKeyBeginning) {
		this.labelKeyBeginning = labelKeyBeginning;
		leftLinks = new ArrayList<SimpleNavbar.LinksGroup>();
		rightLinks = new ArrayList<SimpleNavbar.LinksGroup>();
	}
	
	public static SimpleNavbar of(String labelKeyBeginning) {
		return new SimpleNavbar(labelKeyBeginning);
	}
	
	public void addBrandLink(String labelKey, String destination) {
		this.brandLink = new Link(prependLabelKey(labelKey), destination, false, 0);
	}
	
	public LinksGroup addLeftLinksGroup(String labelKey) {
		LinksGroup newGroup = new LinksGroup(prependLabelKey(labelKey));
		leftLinks.add(newGroup);
		return newGroup;
	}
	
	public SimpleNavbar addLeftLink(String labelKey, String destination, boolean hideSmall, Integer index) {
		LinksGroup newGroup = new LinksGroup(prependLabelKey(labelKey));
		leftLinks.add(newGroup);
		newGroup.add(new Link(prependLabelKey(labelKey), destination, hideSmall, index));
		return this;
	}
	
	public LinksGroup addRightLinksGroup(String labelKey) {
		LinksGroup newGroup = new LinksGroup(prependLabelKey(labelKey));
		rightLinks.add(newGroup);
		return newGroup;
	}
	
	public SimpleNavbar addRightLink(String labelKey, String destination, boolean hideSmall, Integer index) {
		LinksGroup newGroup = new LinksGroup(prependLabelKey(labelKey));
		rightLinks.add(newGroup);
		newGroup.add(new Link(prependLabelKey(labelKey), destination, hideSmall, index));
		return this;
	}
	
	private String prependLabelKey(String key) {
		return labelKeyBeginning + "." + key;
	}
}
