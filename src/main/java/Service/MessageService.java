package Service;


import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;


    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO accountDAO){
        this.messageDAO = accountDAO;
    }

     /**
     * get all messages in the databse 
     * @return list of all message objects
     */

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

     /**
     * add message to database
     * @param Message message
     * @return return the created message object
     */

    public Message addedMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

     /**
     * delete a message from database based on message_id
     * @param int message_id
     * @return the message that was deleted
     */

    public Message deleteMessageByMessageID(int message_id) {
        Message messageDeleted = messageDAO.getMessageByID(message_id);
        boolean messageExists = messageDAO.messageExists(message_id);
        if (messageExists) {
            return messageDeleted;
        }

        return null;
    }

     /**
     * get message based on message_id
     * @param int message_id
     * @return message that was matched by message_id
     */

    public Message getMessageByID(int message_id) {
        return messageDAO.getMessageByID(message_id);
    }

     /**
     * get all messages by posted_by
     * @param int posted_by
     * @return List of all messages that match with the posted_by
     */

    public List<Message> getAllMessagesFromPostedBy(int posted_by) {
        return messageDAO.getMessagesGivenPostedBy(posted_by);
    }
}
