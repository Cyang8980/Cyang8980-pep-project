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

        // app.get("/messages/1",this::getMessageByIDHandler); 
        // app.delete("/messages/{message_id}", this::deleteMessageHandler);

        app.get("/messages/{message_id}",this::getMessageByIDHandler);

        // app.get("/accounts/1/messages",this::getAllMessagesFromUserHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

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

    private void getAllAccountsHandler(Context ctx) {
        List<Account> accounts = accountService.getAllAccounts();
        ctx.json(accounts);
    }

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

    public void getAllMessagesHandler(Context ctx) {
        List<Message> message = messageService.getAllMessages();
        ctx.json(message);
    }




    private void deleteMessageByIDHandler(Context ctx) { 
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        boolean deletionSuccessful = messageService.deleteMessageByMessageID(message_id);
        if (deletionSuccessful) {
            ctx.status(200); // OK
        } else {
            ctx.status(404); // Not Found
        }
    }

    private void getMessageByIDHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(message_id);
    
        if (message != null) {
            ctx.json(message);
            ctx.status(200); // OK
        } else {
            ctx.status(404); // Not Found
        }
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
}