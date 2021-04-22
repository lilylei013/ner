package com.advs.train.core;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;


public class Analyzer {
    public static  String threadTemplate = "page=1&rpp=_entrynumber_&sortby=lastediteddateofthread&sortorder=desc&expand=attachments;entities;entity;aliases;alias;entry;source;submitter;thread;&draft=false&entrytype=_ENTRY_TYPE_&showattachmentsbyentry=true&showpermission=true&outputformat=json";
    private static String baseUrl = "https://tamaletpm1.rms.advent.com";
    private static String threadUrl = "";

    private static final char[] specialCharacters = {',', '!', '?', ':', ';', '-', '+', '@', '%', '(', ')', '='};

    private static String username = "admin";
    private static String password = "admin";
    private static String entrytype = "";
    private static String entrynumber = "";
    private static String datetime = "";
//    private static String pathname = "";

    private static final Logger mLog = Logger.getLogger(Analyzer.class);
    private enum Type {
    	ShortName(1),
		LongName(3),
		Alias(5);

    	int seq;
		Type(int i) {
			seq = i;
		}
	}

	private static final Map<String, Set<String>> aliasMap = new HashMap<>();

    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("Parameter is wrong!");
            System.out.println("USAGE: java -cp ./lib/*:./classes/ com.tamalesoftware.content.Analyzer {baseURL} {username} {password} {entrytype} {entrynumber} {folder}");
            System.out.println("For example:");
            System.out.println("java -cp ./lib/*:./classes/ com.tamalesoftware.content.Analyzer https://tamaletpm1.rms.advent.com admin admin d70218d00a16ada08d6bf55b0b29ef9b 2 2020_07_28__13_01_58");
            System.out.println();

            System.exit(0);
        } else {
            baseUrl = args[0];
            username = args[1];
            password = args[2];
            entrytype = args[3];
            entrynumber = args[4];
            datetime = args[5];
            threadTemplate = threadTemplate.replace("_entrynumber_", entrynumber);
            threadUrl = baseUrl + "/restapi/2.1/thread/";
        }
		// Verify username and password
		String apiUrl = baseUrl + "/restapi/2.1/entry-type/?getdefaulttype=true&entryclass=note&outputformat=json";
		String response = getResponse(apiUrl);
		if (response == null) {
			System.out.println("Wrong user name or password!");
			mLog.fatal("Wrong user name or password!");
			System.exit(-1);
			return;
		}

        String pathname = "/tmp/tokenized_" + datetime;
        for (String entryType : StringUtils.split(entrytype, ",")) {
            processEntryType(entryType, pathname);
        }



        System.out.println("Output folder: " + pathname);

    }

    public static void processEntryType(String entryType, String pathname) {
		String type;
        try {
			type = URLEncoder.encode(entryType, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mLog.error(e.getMessage());
            return;
        }
        String url = threadTemplate.replace("_ENTRY_TYPE_", type);
        JSONObject thread = getThreadInfo(url);
        JSONArray thread_list = thread.optJSONArray("thread-list");
        if (thread_list != null) {
			int length = thread_list.length();
			mLog.info("Processed " + length + " entries for this entry type: " + type + ", specified #: " + entrynumber);
			for (int i = 0; i < length; i ++) {
                JSONObject singleThread = thread_list.getJSONObject(i);
                String id = singleThread.getString("id");
                JSONArray notes = singleThread.getJSONArray("notes");
                JSONArray attachments = singleThread.getJSONArray("attachments");
                Map<String, Type> names = new HashMap<>();
                StringBuilder content = new StringBuilder();

                for (int j = 0; j < notes.length(); j++) {
                    JSONObject note = notes.getJSONObject(j);
                    JSONObject data = note.getJSONObject("data");

					int threadPosition = data.optInt("thread-position", 0);
					if (threadPosition != 0) {
						mLog.info("Skip a side note, id: " + id);
						continue;
					}
                    String title = data.optString("title");
                    mLog.debug("Title: " + title);
                    String noteUrl = data.optJSONObject("body").optJSONObject("link").optString("href");
                    mLog.debug("note body url: " + noteUrl);
                    String noteBody = getNoteBody(noteUrl);

                    append(content, title);
                    String plainBody = null;
                    try {
                        plainBody = EntryXMLEncoder.getInstance().extractPlainBody(noteBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
               
                    append(content, plainBody);

                    // Entities
                    JSONArray entities = data.getJSONObject("entities").optJSONArray("data");
                    if (entities != null && entities.length() > 0) {
                        for (int k = 0; k < entities.length(); k ++) {
                            JSONObject entity = entities.getJSONObject(k).getJSONObject("data");
                            names.put(entity.getString("long-name"), Type.LongName);
                            names.put(entity.getString("short-name"), Type.ShortName);
							String entityId = entity.getString("id");

							Set<String> alias = aliasMap.get(entityId);
                            if (alias == null) {
                            	Set<String> aliasSet = new HashSet<>();
								String entityUrl = "/restapi/2.0/entity/" + entityId + "/aliases/?outputformat=json";
								JSONObject responseObj = getEntityAlias(entityUrl);
								JSONArray aliasData = responseObj.optJSONArray("data");
								if (aliasData != null && aliasData.length() > 0) {
									for (int l = 0; l < aliasData.length(); l ++) {
										String phid = aliasData.getJSONObject(l).getString("phid");
										if (StringUtils.contains(phid, ":")) {
											phid = StringUtils.trim(StringUtils.substringAfterLast(phid, ":"));
										} else if (StringUtils.contains(phid, "_")) {
											phid = StringUtils.trim(StringUtils.substringAfterLast(phid, "_"));
										}
										aliasSet.add(StringUtils.trim(phid));
									}
								}
								aliasMap.put(entityId, aliasSet);
								alias = aliasSet;
							}
                            if (!alias.isEmpty()) {
                            	for (String a : alias) {
                            		names.put(a, Type.Alias);
								}
							}
                        }
                    }





                }

                if (attachments != null && attachments.length() > 0) {
                    for (int j = 0; j < attachments.length(); j++) {
                        JSONObject attachment = attachments.getJSONObject(j).getJSONObject("data");
                        String fileName = attachment.getString("filename");
                        String isEmbeddedImage = attachment.getString("is-embedded-image");
                        String href = baseUrl + attachment.getString("href") + "filedata/";
                        append(content, StringUtils.substringBeforeLast(fileName, "."));
                        String extension = StringUtils.substringAfterLast(fileName, ".").toLowerCase();
                        if (StringUtils.equals(isEmbeddedImage, "false")) {

                            if (fileName.contains(".") && (StringUtils.equalsIgnoreCase(extension, "doc") || StringUtils.equalsIgnoreCase(extension, "docx") ||
                                    StringUtils.equalsIgnoreCase(extension, "msg") || StringUtils.equalsIgnoreCase(extension, "txt") || StringUtils.equalsIgnoreCase(extension, "pdf"))) {
                                byte[] attachmentContent = getAttachment(href);
                                if (attachmentContent == null || attachment.length() == 0 || attachment.length() > 5120000) {
                                    continue;
                                }
                                InputStream stream = null;
                                try {
                                    stream = new ByteArrayInputStream(attachmentContent);
                                    if (stream.available() == 0 || stream.available() > 5120000) {
                                        continue;
                                    }
                                    BodyContentHandler handler = new BodyContentHandler(-1);

                                    TikaConfig config = new TikaConfig(Analyzer.class.getResourceAsStream("/Tika-config.xml"));

                                    AutoDetectParser parser = new AutoDetectParser(config);
                                    Metadata metadata = new Metadata();
                                    parser.parse(stream, handler, metadata);
                                    String fileContent = handler.toString();
                                    mLog.debug("File content: " + fileContent);
                                    append(content, fileContent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (stream != null) {
                                        try {
                                            stream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                        }

                    }
                }
                System.out.println("content: " + content.toString());
				tokenize(content.toString(), names, id, pathname);
            }
        }
    }

    private static void tokenize(String contentStr, Map<String, Type> names, String id, String pathname) {
        mLog.debug("names: " + names);
        if (StringUtils.isBlank(contentStr)) {
            mLog.debug("Entry is empty: " + id);
            return;
        }
        Reader r = new StringReader(contentStr);
        PTBTokenizer<Word> tokenizer = PTBTokenizer.newPTBTokenizer(r);
        List<String> l = new ArrayList<>();
		List<String> lastMatchedShortNames = new LinkedList<>();
		List<String> lastMatchedLongNames = new LinkedList<>();
		List<String> lastMatchedAlias = new LinkedList<>();
        while (tokenizer.hasNext()) {
            Word w = tokenizer.next();
            String word = w.word();

			/*
			 * 1 short name match
			 * 2 short name part match
			 * 3 long name match
			 * 4 long name part match
			 * 5 alias match
			 * 0 not match
			 */
			int matched = 0;
			/*
			  for short name, if it contains multiple words, need the token full match the short name.
			  for long name, if it contains multiple words, no need full match, instead, any token starts with part of the name should be tagged.
			  for long name, if it contains multiple words, part of the match should not be stop word:
			  	1, THE PRESIDENT AND TRUSTEES OF WILLIAMS COLLEGE, 'THE' should be tagged.
			  	2, The man is driving a car. 'The' should not be tagged.
			  for alias, if a word ends with
			 */
			for (Map.Entry<String, Type> entityPair : names.entrySet()) {
            	String entity = entityPair.getKey();
				Type entityType = entityPair.getValue();
            	if (entity.contains(" ")) {
					if (StringUtils.equalsIgnoreCase(entity.substring(0, entity.indexOf(" ")), word)) {
            			matched = entityType.seq + 1;
            			break;
					} else if (!lastMatchedShortNames.isEmpty()) {
						String prev = StringUtils.join(lastMatchedShortNames, " ").replace(" ,", ",");
						String now = word.equals(",") ? prev + "," : prev + " " + word;
						if (StringUtils.equalsIgnoreCase(entity, now)) {
							matched = entityType.seq;
							break;
						} else if (StringUtils.startsWithIgnoreCase(entity, now)) {
							matched = entityType.seq + 1;
							break;
						}
					} else if (!lastMatchedLongNames.isEmpty()) {
						String prev = StringUtils.join(lastMatchedLongNames, " ").replace(" ,", ",");
						String now = word.equals(",") ? prev + "," : prev + " " + word;
						if (StringUtils.equalsIgnoreCase(entity, now)) {
							matched = entityType.seq;
							break;
						} else if (StringUtils.startsWithIgnoreCase(entity, now)) {
							matched = entityType.seq + 1;
							break;
						}
					} else if (!lastMatchedAlias.isEmpty()) {
						String prev = StringUtils.join(lastMatchedAlias, " ").replace(" ,", ",");
						String now = word.equals(",") ? prev + "," : prev + " " + word;
						if (StringUtils.equalsIgnoreCase(entity, now)) {
							matched = entityType.seq;
							break;
						} else if (StringUtils.startsWithIgnoreCase(entity, now)) {
							matched = entityType.seq + 1;
							break;
						}
					}
				} else {
					if (word.length() > 1 && StringUtils.containsNone(word, specialCharacters) && (StringUtils.equalsIgnoreCase(entity, word))) {
						matched = entityType.seq;
						break;
					}
					if (entityType == Type.Alias && StringUtils.equalsIgnoreCase(entity, word)) {
						matched = entityType.seq;
						break;
					}
				}
            }
            if (matched == Type.ShortName.seq) {
				lastMatchedShortNames.add(word);
				for (String item : lastMatchedShortNames) {
					l.add(item + "\tORGANIZATION");
				}
				lastMatchedShortNames.clear();
            } else if (matched == Type.LongName.seq) {
            	lastMatchedLongNames.add(word);
			} else if (matched == Type.ShortName.seq + 1) {
				lastMatchedShortNames.add(word);
			} else if (matched == Type.LongName.seq + 1) {
				lastMatchedLongNames.add(word);
			} else if (matched == Type.Alias.seq) {
				lastMatchedAlias.add(word);
				for (String item : lastMatchedAlias) {
					l.add(item + "\tORGANIZATION");
				}
				lastMatchedAlias.clear();
			} else if (matched == Type.Alias.seq + 1) {
				lastMatchedAlias.add(word);
			} else {
            	if (!lastMatchedShortNames.isEmpty()) {
            		for (String item : lastMatchedShortNames) {
						l.add(item + "\tO");
					}
					lastMatchedShortNames.clear();
				}
            	if (!lastMatchedLongNames.isEmpty()) {
					String longName = StringUtils.join(lastMatchedLongNames, " ");
					boolean legal = !StringUtils.equalsIgnoreCase(longName, "the") && !StringUtils.equalsIgnoreCase(longName, "and") && !StringUtils.equalsIgnoreCase(longName, "of") &&
							!StringUtils.equalsIgnoreCase(longName, "as") && !StringUtils.equalsIgnoreCase(longName, "for") && !StringUtils.equalsIgnoreCase(longName, "may") &&
							!StringUtils.equalsIgnoreCase(longName, "weekly") && !StringUtils.equalsIgnoreCase(longName, "please") && !StringUtils.equalsIgnoreCase(longName, "have") &&
							!StringUtils.equalsIgnoreCase(longName, "she") && !StringUtils.equalsIgnoreCase(longName, "am") &&
							!StringUtils.equalsIgnoreCase(longName, "a") && !StringUtils.equalsIgnoreCase(longName, "an");
            		for (String item : lastMatchedLongNames) {
						if (legal) {
							l.add(item + "\tORGANIZATION");
						} else {
							l.add(item + "\tO");
						}
					}
					lastMatchedLongNames.clear();
				}
				if (!lastMatchedAlias.isEmpty()) {
					for (String item : lastMatchedAlias) {
						l.add(item + "\tO");
					}
					lastMatchedAlias.clear();
				}

				l.add(word + "\tO");
			}
        }

        File folder = new File(pathname);
        if (!folder.exists()) {
            mLog.info("Creating a folder for tokenization: " + pathname);
            boolean success = folder.mkdir();
            if (!success) {
            	mLog.error("Failed to create folder: " + pathname);
            	System.exit(-1);
			}
        }
        try (PrintWriter writer = new PrintWriter( folder.getAbsolutePath() + "/" + id + ".txt", "UTF-8")) {
            for (String line : l) {
                writer.println(line);
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
			mLog.error("Unable to write file: " + pathname, e);
        }
	}

    private static JSONObject getThreadInfo(String threadTemplate) {
        RestRequest<String> request = new RestRequest<>(String.class);
        request.setUsername(username);
        request.setPassword(password);
        request.setUrl(threadUrl);
        mLog.debug("Url: " + threadUrl);
        System.out.println("Url: " + threadUrl);
        request.setPostBody("advfilter=%28%28entry-class+equals+%22note%22%29+and+%28%28%21+entities+contains+long-name+%22EMAIL_SPOOLER%22%29+and+%28%21+entities+contains+id+%2291a717ed56ef3a471dd5f339b85dbcce%22%29%29%29");
        request.setQuery(threadTemplate);
        System.out.println("query: " + threadTemplate);
        request.setMethod("POST");
        try {
            String response = makeStringCallWrapper(request);
            mLog.debug("Response: " + response);
            mLog.debug(response);
            System.out.println(response);
            return new JSONObject(response);
        } catch (Exception e) {
            mLog.error("Exception trying to access Tamale service!", e);
            return new JSONObject();
        }
    }

	public static String getResponse(String url) {
		RestRequest<String> request = new RestRequest<>(String.class);
		request.setUsername(username);
		request.setPassword(password);
		request.setUrl(url);
		request.setMethod("GET");
		try {
			String response = makeStringCallWrapper(request);
			mLog.info("Get entry types: " + response);
			return response;
		} catch (Exception e) {
			mLog.error("Exception trying to access Tamale service!" + e.getMessage());
			return null;
		}
	}

    public static String getNoteBody(String noteUrl) {
        RestRequest<String> request = new RestRequest<>(String.class);
        request.setUsername(username);
        request.setPassword(password);
        request.setUrl(baseUrl + noteUrl);
        request.setMethod("GET");
        try {
            String response = makeStringCallWrapper(request);
            mLog.debug(response);
            return response;
        } catch (Exception e) {
            mLog.error("Exception trying to access Tamale service!", e);
            return null;
        }
    }

    private static JSONObject getEntityAlias(String url) {
        RestRequest<String> request = new RestRequest<>(String.class);
        request.setUsername(username);
        request.setPassword(password);
        request.setUrl(baseUrl + url);
        request.setMethod("GET");
        try {
            String response = makeStringCallWrapper(request);
            mLog.debug(response);
            return isJSON(response) ? new JSONObject(response) : new JSONObject();
        } catch (Exception e) {
            mLog.error("Exception trying to access Tamale service!", e);
            return new JSONObject();
        }
    }

	public static boolean isJSON(String str) {
		return !StringUtils.isBlank(str) && str.startsWith("{") && str.endsWith("}");
	}

    public static void append(StringBuilder sb, String content) {
        if (StringUtils.isNotBlank(content)) {
            content = content.trim();
            sb.append(content);
            if (Character.isLetterOrDigit(content.charAt(content.length() - 1))) {
                sb.append(". ");
            } else {
                sb.append(" ");
            }
        }
    }

    private static String makeStringCallWrapper(RestRequest<String> pRequest) {
        String returnVal = null;
        try {
            RestAccessor<String> accessor = new RestAccessor<>(pRequest);
            returnVal = accessor.makeStringCall();
        } catch (Exception iex) {
            mLog.error("IO Error: ", iex);
        }
        return returnVal;
    }

    public static byte[] getAttachment(String noteUrl) {
        RestRequest<String> request = new RestRequest<>(String.class);
        request.setUsername(username);
        request.setPassword(password);
        request.setUrl(noteUrl);
        request.setMethod("GET");
        try {
            byte[] response = makeByteCallWrapper(request);
            mLog.debug(response);
            return response;
        } catch (Exception e) {
            mLog.error("Exception trying to access Tamale service!", e);
            return null;
        }
    }

    private static byte[] makeByteCallWrapper(RestRequest<String> pRequest) {
        byte[] returnVal = null;
        try {
            RestAccessor<String> accessor = new RestAccessor<>(pRequest);
            returnVal = accessor.makeBytesCall(mLog);
        } catch (Exception iex) {
            mLog.error("IO Error: ", iex);
        }
        return returnVal;
    }
    
    public static void setBaseUrl(String url) {
    	baseUrl = url;
    	threadTemplate = threadTemplate.replace("_entrynumber_", "200");
        threadUrl = baseUrl + "/restapi/2.1/thread/";
    }
}
