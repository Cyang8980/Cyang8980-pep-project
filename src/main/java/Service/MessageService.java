package Service;


import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

public class AccountService {
    private MessageDAO messageDAO;


    public AccountService(){
        messageDAO = new MessageDAO();
    }

    public AccountService(MessageDAO accountDAO){
        this.messageDAO = accountDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message addAccount(Message message) {
        return messageDAO.insertMessage(message);
    }
}
}
