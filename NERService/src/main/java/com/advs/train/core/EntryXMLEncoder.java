package com.advs.train.core;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.Translate;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public class EntryXMLEncoder {

    public final static String TableStartTag = "<!-- START: custom table metadata -->";
    public final static String TableEndTag = "<!-- END: custom table metadata -->";
    public final static String OldTableStartTag = "<!-- START: custom legal metadata -->";
    public final static String OldTableEndTag = "<!-- END: custom legal metadata -->";
    public final static String EntityUpdateStartTag = "<!-- START: Entity Update Summary -->";
    public final static String EntityUpdateEndTag = "<!-- END: Entity Update Summary -->";
    public final static String SubjectStartTag = "<!-- START: subject begin -->";
    public final static String SubjectEndTag = "<!-- END: subject end -->";

    public static int affectedEntryCounter = 0;
    private static final String FLAG_KEY = "transform maxblurb";
    private static Charset mSafeCharset = StandardCharsets.UTF_8;
    private static EntryXMLEncoder ee = null;

    public static EntryXMLEncoder getInstance() {
        if (ee == null) {
            ee = new EntryXMLEncoder();
        }
        return ee;
    }

    /**
     * Parses the given html and gets only the <body>*</body> element
     */
    public String extractBody(String pHtml) throws Exception {
        BodyTag bt = getBodyTag(pHtml);
        return bt == null ? pHtml : bt.getStringText();
    }


    // Does the real work for the convenience method above.
    public String extractBlurb(String pHtml, int pMaxBlurbLength) throws Exception {
        // Guard
        if (null == pHtml) {
            return null;
        }
        pHtml = filterAdhocFieldFromHtml(pHtml);
        pHtml = filterSubjectFromHtml(pHtml);
        String body = extractBody(pHtml);
        String blurb = getPlainBody(body);

        return truncateBlurb(blurb, pMaxBlurbLength);
    }

    public String extractPlainBody(String pHtml) throws Exception {
        // Guard
        if (null == pHtml) {
            return null;
        }
        pHtml = filterAdhocFieldFromHtml(pHtml);
        pHtml = filterSubjectFromHtml(pHtml);
        String body = extractBody(pHtml);
        String plainBody = getPlainBody(body);

        return plainBody;
    }


    public String filterAdhocFieldFromHtml(String pHtml) throws Exception {
        if (pHtml.indexOf(TableStartTag) != -1 && pHtml.indexOf(TableEndTag) != -1) {
            pHtml = pHtml.substring(0, pHtml.indexOf(TableStartTag)) + pHtml.substring(pHtml.indexOf(TableEndTag) + TableEndTag.length());
        }

        if (pHtml.indexOf(OldTableStartTag) != -1 && pHtml.indexOf(OldTableEndTag) != -1) {
            pHtml = pHtml.substring(0, pHtml.indexOf(OldTableStartTag)) + pHtml.substring(pHtml.indexOf(OldTableEndTag) + OldTableEndTag.length());
        }

        if (pHtml.indexOf(EntityUpdateStartTag) != -1 && pHtml.indexOf(EntityUpdateEndTag) != -1) {
            pHtml = pHtml.substring(0, pHtml.indexOf(EntityUpdateStartTag)) + pHtml.substring(pHtml.indexOf(EntityUpdateEndTag) + EntityUpdateEndTag.length());
        }

        return pHtml;
    }

    public String filterSubjectFromHtml(String pHtml) {
        if (pHtml.indexOf(SubjectStartTag) > -1 && pHtml.indexOf(SubjectEndTag) > -1) {
            pHtml = StringUtils.substringBefore(pHtml, SubjectStartTag) + StringUtils.substringAfter(pHtml, SubjectEndTag);
        }
        return pHtml;

    }

    public static String StripInvalidCharacters(String in ){
        StringBuilder sb = new StringBuilder();
        for (int ch : in.codePoints().toArray()) {
            if ((ch == 0x9) || (ch == 0xA) || (ch == 0xD)
                    || ((ch >= 0x20) && (ch <= 0xD7FF))
                    || ((ch >= 0xE000) && (ch <= 0xFFFD))
                    || ((ch >= 0x10000) && (ch <= 0x10FFFF))) {
                sb.appendCodePoint(ch);
            }
        }
        return sb.toString();
    }

    /*
     * Truncates an existing blurb to the designated size.
     * pBlurb must *not* be raw HTML! Use extractBlurb for that.
     *
     */
    public String truncateBlurb(String pBlurb, int pMaxBlurbLength) {
        //pBlurb include :title,adhocfile,notebody
        // Fix for https://jiraprod.advent.com/browse/TAM-7538
        pBlurb = StripInvalidCharacters(pBlurb);

        if (pBlurb.length() <= pMaxBlurbLength) {
            return pBlurb;
        }

        String smallBlurb = pBlurb;
        smallBlurb = smallBlurb.substring(0, pMaxBlurbLength - 3); // -3 because we append "..."
        // find the last space so we don't truncate in the middle of a word
        int lastSpace = smallBlurb.lastIndexOf(' ');
        if (lastSpace != -1) {
            smallBlurb = smallBlurb.substring(0, lastSpace);
        }
        smallBlurb += "...";
        return smallBlurb;
    }




    /**
     * Extracts the plain text version (remove all html formatting) of the <body>*</body> element of pHtml
     * <p>
     * N.B. This is a critical method for compatibility with the client. We need to generate a UTF-8 string, suitable for use in webservice
     * replies. First we need to get the string data from the note. Then we need to translate all the escape sequences (&nbsp; etc.) to
     * characters. Finally, we need to ensure that the string is ASCII compatible for .Net webservice clients. Since java is natively UTF 16,
     * this requires conversion.
     * <p>
     * Reference - http://java.sun.com/j2se/corejava/intl/reference/faqs/ The Java programming language is based on the Unicode character set,
     * and several libraries implement the Unicode standard. The primitive data type char in the Java programming language is an unsigned 16-bit
     * integer that can represent a Unicode code point in the range U+0000 to U+FFFF, or the code units of UTF-16. The various types and classes
     * in the Java platform that represent character sequences - char[], implementations of java.lang.CharSequence (such as the String class),
     * and implementations of java.text.CharacterIterator - are UTF-16 sequences.
     */
    public String getPlainBody(String pHtml) throws Exception {
        if (pHtml == null) {
            return "";
        }

        String text = getPlainText(pHtml);

        // sometimes you will have html embedded in html as
        // escape sequences. These are converted back to html
        // by the get plaintext call. So we loop until there are
        // no further conversions.
        for (int i = 0; i < 10; i++) {
            String tempText = getPlainText(text);
            // compare ignoring whitespace, to avoid inadvertantly removing valid newlines
            if (tempText.replaceAll("\\s", "").equalsIgnoreCase(text.replaceAll("\\s", ""))) {
                break;
            }
            text = tempText;
        }

        // convert the utf-16 string to ascii
        CharsetEncoder enc = mSafeCharset.newEncoder();

        enc.onMalformedInput(CodingErrorAction.REPLACE);
        enc.onUnmappableCharacter(CodingErrorAction.REPLACE);
        // instead of the default ? replacement, use space
        byte replacement[] = {(byte) ' '};
        enc.replaceWith(replacement);

        ByteBuffer bb = enc.encode(CharBuffer.wrap(text.toCharArray()));
        CharBuffer cb = mSafeCharset.decode(bb);

        filterISOControlCharacters(cb.array());

        String rText = cb.toString();
        return rText;
    }

    /**
     * 'anyISOControlCharactersOrInvalidXMLCharacters' method returns true if there is one or more
     * ISOControl characters or invalid XML characters within pString.
     * See the standard: http://www.w3.org/TR/REC-xml/#dt-character
     *     *
     * @param pString a value of type 'String'
     * @return a value of type 'boolean'
     */
    /**
     * Describe 'anyInvalidOrDiscouragedXMLCharacters' method here.
     *
     * @param pString a value of type 'String'
     * @return a value of type 'boolean'
     */

    public static boolean anyInvalidOrDiscouragedXMLCharacters(String pString)
    {
        // Guard
        if(null == pString || pString.length() == 0) return false;

        // Average case is that nothing needs to change. Thus
        // check the string first before tossing garbage onto the garbage collector.
        for(int i = 0, j = pString.length(); i < j; i++)
        {
            char ch = pString.charAt(i);
            if(Character.isISOControl(ch) || ch==0x000A || ch==0x000D || ch==0x0009 || ch == '?'
                    || (ch >= 0xFDD0 && ch <= 0xFDEF) )    // Character.MAX_VALUE = '\uFFFF'
            {
                return true;
            }
        }


        return false;
    }

    /**
     * Remove ISO Control codes from teh 16 bit unicode char array at the char level. Efficient.
     *
     * @param parray a value of type 'char'
     * @return a value of type 'char[]'
     */

    public static char[] filterISOControlCharacters(char[] parray)
    {
        if(null == parray ) return parray;

        ///////////////////////////////////////////////////////////////////////////////////////////////
        // Remove any ISO control characters with the exception of linefeed, carriage return and tab //
        ///////////////////////////////////////////////////////////////////////////////////////////////
        char[] array  = parray;
        for(int i = 0,j = array.length; i < j; i++)
        {
            char ch = array[i];
            if(Character.isISOControl(ch) &&
                    !(ch==0x000A || ch==0x000D || ch==0x0009))
            {
                array[i] = ' ';
            }
        }

        return array;
    }

    /**
     * Remove ISO Control points from the 16 bit unicode String.
     *
     * @param pString a value of type 'String'
     * @return a value of type 'String'
     */

    public static String filterISOControlCharactersFromXML(String pString)
    {
        // In perl you can do numeric ranges, in hex no less, but not java. This is actually faster than it would appear
        // as the groups are OR'ed and thus the finite state machine will terminate when the first group matches.
        return pString.replaceAll("(&#x0;)|(&#x1;)|(&#x2;)|(&#x3;)|(&#x4;)|(&#x5;)|(&#x6;)|(&#x7;)|(&#x8;)|(&#xB;)|(&#xC;)|(&#xE;)|(&#xF;)|(&#x10;)|(&#x11;)|(&#x12;)|(&#x13;)|(&#x14;)|(&#x14;)|(&#x15;)|(&#x16;)|(&#x17;)|(&#x18;)|(&#x19;)|(&#x1A;)|(&#x1B;)|(&#x1C;)|(&#x1D;)|(&#x1E;)|(&#x1F;)|(&#x7F;)|(&#x80;)|(&#x81;)|(&#x82;)|(&#x83;)|(&#x84;)|(&#x86;)|(&#x87;)|(&#x88;)|(&#x89;)|(&#x8A;)|(&#x8B;)|(&#x8C;)|(&#x8D;)|(&#x8E;)|(&#x8F;)|(&#x90;)|(&#x91;)|(&#x92;)|(&#x93;)|(&#x94;)|(&#x95;)|(&#x96;)|(&#x97;)|(&#x98;)|(&#x99;)|(&#x9A;)|(&#x9B;)|(&#x9C;)|(&#x9D;)|(&#x9E;)|(&#x9F;)|(&#xFFFE;)|(&#xFFFF;)"," ");
    }

    public String getPlainText(String pHtml) throws Exception {
        // extract plain text from the body
        StringBean sb = new StringBean();
        Parser p = new Parser();
        p.setInputHTML(pHtml);
        sb.setLinks(false);
        sb.setReplaceNonBreakingSpaces(true);
        sb.setCollapse(true);
        p.visitAllNodesWith(sb);
        String text = sb.getStrings();
        // sanity check
        if (text == null) {
            text = "";
        }

        text = cleanBadCharacters(text);

        // turn escape characters into unicode characters
        text = Translate.decode(text).trim();

        return text;
    }

    /**
     * Cleans up bad characters from the HTML.
     *
     * @param pText
     * @return
     */
    public String cleanBadCharacters(String pText) {
        int badCharStart = -1;
        while ((badCharStart = pText.indexOf("<?", badCharStart + 1)) != -1) {
            int badCharEnd = pText.indexOf("/>", badCharStart);
            if (badCharEnd == -1) {
                badCharEnd = pText.indexOf("?>", badCharStart);
            }
            if (badCharEnd != -1) {
                pText = pText.substring(0, badCharStart) + pText.substring(badCharEnd + 2);
            }
        }

        return pText;
    }



    /**
     * Convenience method to get the body of the html. If pHtml is null, returns an empty String
     */
    protected BodyTag getBodyTag(String pHtml) throws Exception {
        if (pHtml == null) {
            pHtml = getHeader() + getFooter();
        }
        Parser p = new Parser();
        p.setInputHTML(pHtml);
        NodeList bodyNodes = p.extractAllNodesThatMatch(new TagNameFilter("body"));
        if (bodyNodes != null && bodyNodes.size() > 0) {
            return (BodyTag) bodyNodes.elementAt(0);
        } else {
            return null;
        }
    }

    /**
     * @return The header for an html note
     */
    public String getHeader() {
        return "<html><head><META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\"></head><body>";
    }

    /**
     * @return The footer for an html note
     */
    public String getFooter() {
        return "</body></html>";
    }

}
