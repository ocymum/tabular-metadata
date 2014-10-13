
package com.asoroka.sidora.tabularmetadata.datatype;

import static com.asoroka.sidora.tabularmetadata.datatype.Lex.LEX;
import static java.util.regex.Pattern.compile;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class URI extends XSDAssociatedType<URI, Lex> {

    public static final URI URI = new URI();

    private URI() {
        super(LEX, W3C_XML_SCHEMA_NS_URI + "#anyURI");
    }

    @Override
    public URI self() {
        return this;
    }

    /**
     * We use the well-known regex from <a href="http://tools.ietf.org/html/rfc3986#appendix-B">the standard</a> but
     * we disallow relative URIs.
     */
    static Pattern URI_REGEX = compile("^(([^:/?#]+):)(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");

    @SuppressWarnings("unchecked")
    @Override
    public java.net.URI parse(final String s) throws ParsingException {
        try {
            if (URI_REGEX.matcher(s).matches()) {
                return new java.net.URI(s);
            }
            throw new URISyntaxException(s, "Could not validate URI!");
        } catch (final URISyntaxException e) {
            throw new ParsingException("Could not parse as URI!", e);
        }
    }

}
