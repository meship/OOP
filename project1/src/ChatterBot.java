import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 * @author Dan Nirel
 */
class ChatterBot {
	static final String REQUEST_PREFIX = "say ";
	static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
	static final String R_REQUEST_ILLEGAL = "<request>";

	Random rand = new Random();
	String name;
	String[] repliesToLegalRequest;
	String[] repliesToIllegalRequest;


	ChatterBot(String name,String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
		this.name = name;
		this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
		for(int i =0; i<repliesToLegalRequest.length;i++){
			this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
		}

		this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
		for(int i = 0 ; i < repliesToIllegalRequest.length ; i = i+1) {
			this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
		}
	}

	String replyTo(String statement) {
		if(statement.startsWith(REQUEST_PREFIX)) {
			//we don’t repeat the request prefix, so delete it from the reply
			return replacePlaceholderInARandomPattern(repliesToLegalRequest, statement, REQUESTED_PHRASE_PLACEHOLDER);

		}
		return replacePlaceholderInARandomPattern(repliesToIllegalRequest, statement, R_REQUEST_ILLEGAL);
	}

	String respondToIllegalRequest(String statement) {
		int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
		String reply = repliesToIllegalRequest[randomIndex];
		reply = reply.replaceAll(R_REQUEST_ILLEGAL, statement);
		return reply;
	}

	String respondToLlegalRequest(String statement) {
		int randomIndex = rand.nextInt(repliesToLegalRequest.length);
		String reply = repliesToLegalRequest[randomIndex];
		reply = reply.replaceAll(REQUESTED_PHRASE_PLACEHOLDER, statement);
		return reply;
	}

	public String getName(){
		return this.name;
	}

	String replacePlaceholderInARandomPattern(String[] replies, String statement,String phrase){
		int randomIndex = rand.nextInt(replies.length);
		String reply = replies[randomIndex];
		reply = reply.replaceAll(phrase, statement);
		return reply;
	}
}

