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

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message addedMessage(Message message) {
        return messageDAO.insertMessage(message);
    }
}
