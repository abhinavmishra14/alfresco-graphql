package fr.smile.alfresco.graphql.query;

import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.io.IOUtils;

import com.sun.xml.messaging.saaj.util.ByteInputStream;

import fr.smile.alfresco.graphql.helper.QueryContext;
import graphql.schema.DataFetchingEnvironment;

public class ContentDataQL extends AbstractQLModel {

	private NodeRef nodeRef;
	private QName property;
	private ContentData contentData;

	public ContentDataQL(QueryContext queryContext, NodeRef nodeRef, QName property, ContentData contentData) {
		super(queryContext);
		this.nodeRef = nodeRef;
		this.property = property;
		this.contentData = contentData;
	}

	public String getProperty() {
		return property.toPrefixString(getNamespaceService());
	}
	public String getMimetype() {
		return contentData.getMimetype();
	}
	
	public int getSize() {
		if (contentData.getSize() > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) contentData.getSize();
	}
	public String getSizeHumanReadable() {
		long bytes = contentData.getSize();
		long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
		if (absB < 1024) {
			return bytes + " B";
		}
		long value = absB;
		CharacterIterator ci = new StringCharacterIterator("KMGTPE");
		for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
			value >>= 10;
			ci.next();
		}
		value *= Long.signum(bytes);
		return String.format("%.1f %ciB", value / 1024.0, ci.current());
	}
	
	public String getEncoding(DataFetchingEnvironment env) {
		return contentData.getEncoding();
	}
	public Optional<String> getLocale() {
		return Optional.ofNullable(contentData.getLocale())
				.map(locale -> locale.toString());
	}

	public String getAsString(DataFetchingEnvironment env) {
		String setValue = env.getArgument("setValue");
		if (setValue != null) {
			getWriter(env).putContent(setValue);
		}
		return getContentService().getReader(nodeRef, property).getContentString();
	}
	public String getAsBase64(DataFetchingEnvironment env) throws IOException {
		String setValue = env.getArgument("setValue");
		if (setValue != null) {
			byte[] buf = Base64.getDecoder().decode(setValue);
			getWriter(env).putContent(new ByteInputStream(buf, buf.length));
		}

		try (InputStream input = getContentService().getReader(nodeRef, property).getContentInputStream()) {
			byte[] buf = IOUtils.toByteArray(input);
			return Base64.getEncoder().encodeToString(buf);
		}
	}
	public String getAsDataUrl(DataFetchingEnvironment env) throws IOException {
		return "data:" + getMimetype() + ";base64," + getAsBase64(env); 
	}
	
	private ContentWriter getWriter(DataFetchingEnvironment env) {
		ContentWriter writer = getContentService().getWriter(nodeRef, property, true);
		
		String encoding = env.getArgument("setEncoding");
		if (encoding != null) {
			writer.setEncoding(encoding);
		}

		String locale = env.getArgument("setLocale");
		if (locale != null) {
			writer.setLocale(new Locale(locale));
		}

		String mimetype = env.getArgument("setMimetype");
		if (mimetype != null) {
			writer.setMimetype(mimetype);
		}
		return writer;
	}
	
	public String getDownloadUrl() {
		return getQueryContext().getDocumentLinkHelper().getDownloadUrl(nodeRef, property);
	}
}