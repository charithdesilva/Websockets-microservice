package sample;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/*
 * Chat Controller listens for chat topic and responds with a message.
 *
 * @Author Jay Sridhar
 */
@Controller
public class ChatController 
{
	private SimpMessagingTemplate template;
	private Logger logger = Logger.getLogger(ChatController.class);

	@Autowired
	public ChatController(SimpMessagingTemplate template) {		
		this.template = template;
	}

    @MessageMapping("/chat/v5/{userId}")
    public void send_v5(@DestinationVariable("userId") String userId,
			      Message message,
			      java.security.Principal loggedInUser) throws Exception
    {
    		logger.info("loggedInUser : " + loggedInUser);
	    	OutputMessage outputMessage = new OutputMessage(loggedInUser.getName(), message.getText(), userId);

	    	if ("global".equals(userId.toLowerCase())) {
	    		this.template.convertAndSend("/topic/global/messages", outputMessage);
	    	}
	    	else {
	    		this.template.convertAndSendToUser(userId, "/topic/messages", outputMessage);
	    	}
    }

    @MessageMapping("/chat/v4/{userId}")
    public void send_v4(@DestinationVariable("userId") String userId,
			      Message message,
			      java.security.Principal loggedInUser) throws Exception
    {
    		logger.info("loggedInUser : " + loggedInUser);
	    	OutputMessage outputMessage = new OutputMessage(loggedInUser.getName(), message.getText(), userId);
	    		this.template.convertAndSendToUser(userId, "/queue/messages", outputMessage);
    }

    @MessageMapping("/chat/v3/{userId}")
    public void send_v3(@DestinationVariable("userId") String userId,
			      Message message,
			      java.security.Principal loggedInUser) throws Exception
    {
    		logger.info("loggedInUser : " + loggedInUser);
	    	OutputMessage outputMessage = new OutputMessage(loggedInUser.getName(), message.getText(), userId);
	    		this.template.convertAndSendToUser(userId, "/topic/messages", outputMessage);
    }

    @MessageMapping("/chat/v2/{userId}")
    public void send_v2(@DestinationVariable("userId") String userId,
			      Message message) throws Exception
    {
	    	OutputMessage outputMessage = new OutputMessage(message.getFrom(), message.getText(), userId);
	    		this.template.convertAndSend("/topic/" + userId, outputMessage);
    }

    @MessageMapping("/chat/v1/{topic}")
    @SendTo("/topic/messages")
    public OutputMessage send_v1(@DestinationVariable("topic") String topic,
			      Message message) throws Exception
    {
    		return new OutputMessage(message.getFrom(), message.getText(), topic);
    }
}
