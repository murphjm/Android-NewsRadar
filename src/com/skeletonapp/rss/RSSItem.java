package com.skeletonapp.rss;

import java.io.Serializable;

public class RSSItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String _title = null;
	private String _description = null;
	private String _content = null;
	private String _link = null;
	private String _category = null;
	private String _pubdate = null;

	RSSItem() {
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setLink(String link) {
		_link = link;
	}

	public void setCategory(String category) {
		_category = category;
	}

	public void setPubDate(String pubdate) {
		_pubdate = pubdate;
	}

	public String getTitle() {
		return _title;
	}

	public String getDescription() {
		return _description;
	}
	
	public String getContent() {
		return _content;
	}

	public void setContent(String _content) {
		this._content = _content;
	}

	public String getLink() {
		return _link;
	}

	public String getCategory() {
		return _category;
	}

	public String getPubDate() {
		return _pubdate;
	}

	public String toString() {
		// limit how much text we display
		if (_title.length() > 42) {
			return _title.substring(0, 42) + "...";
		}
		return _title;
	}

	public int describeContents() {
		return 0;
	}
}
