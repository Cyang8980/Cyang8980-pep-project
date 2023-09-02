package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;


    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/accounts", this::getAllAccountsHandler); // not tested
        app.post("/register", this::postAccountHandler); // works
        app.get("/messages", this::getAllMessagesHandler); // works
        app.post("/messages", this::postMessageHandler); // works
        app.post("/login", this::accountLogin); // works
        app.get("/messages/{message_id}", this::getMessageByIDHandler); // works
        app.patch("/messages/{message_id}", this::updateMessageHandler); 
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler); // works
        app.get("/accounts/{posted_by}/messages",this::getAllMessagesPostedBy); // works
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    /**
     * create a handler that handles creating accounts
     * @param context that contains an account object
     * @return the added account as if account was successfully created and status 400 if not
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    /**
     * create a handler that gets all accounts that exist in the database
     * An ISBN will be provided in Book. Method should check if the book ISBN already exists before it attempts to
     * persist it.
     * @param context a context object
     * @return an arraylist of all accounts that exist
     */

    private void getAllAccountsHandler(Context ctx) {
        List<Account> accounts = accountService.getAllAccounts();
        ctx.json(accounts);
    }

    /**
     * create a handler that reads info and writes a new message
     * a message body will be provided to create the new message
     * @param context a context object
     * @return the message if the message was created successfully
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addedMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> message = messageService.getAllMessages();
        ctx.json(message);
    }

    private void accountLogin(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
    
        String username = account.getUsername();
        String password = account.getPassword();
    
        Account loggedInAccount = accountService.login(username, password);
    
        if (loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
            ctx.status(200); // OK
        } else {
            ctx.status(401); // Unauthorized
        }
    }

    private void getMessageByIDHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message gotMessage = messageService.getMessageByID(message_id);
    
        if (gotMessage != null) {
            ctx.json(gotMessage);
        }
        ctx.status(200);
    }

    private void deleteMessageByIDHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message gotMessage = messageService.deleteMessageByMessageID(message_id);
        if (gotMessage != null) {
            ctx.json(gotMessage);
        }
        ctx.status(200);
    }

    private void updateMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        try {
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);

            Message gotMessage = messageService.getMessageByID(message_id);
            if (gotMessage != null && message.getMessage_text() != "" && message.getMessage_text().length() < 254) {
                    gotMessage.setMessage_text(message.getMessage_text());    
                    ctx.json(gotMessage);
                    ctx.status(200);
            } else {
                ctx.status(400); // Not Found
            }
        } catch (NumberFormatException e) {
            ctx.status(400); // Bad Request
            ctx.result("Invalid message_id format");
        }
    }

    private void getAllMessagesPostedBy(Context ctx) {
        int posted_by = Integer.parseInt(ctx.pathParam("posted_by"));
        List<Message> messages = messageService.getAllMessagesFromPostedBy(posted_by);
        if (messages != null) {
            ctx.json(messages);
        }
        ctx.status(200);
    }

}