package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * message creations, message updates, and message deletions
 */

public class MessageDAO {
    
    /**
     * get all messages that exist in the database
     * @return A list of all message that exist in the database
     */
    
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                        );
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    /**
     * deleting a message based on message_id
     * call the getMessageByID method to obtain the message based on message_ID
     * doing this you check if the message exists and then you delete it
     * @param int message_id
     * @return the message that was deleted
     */
    
    public Message deleteMessageByMessageID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            if (!messageExists(message_id)) {
                return null;
            }
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            preparedStatement.executeUpdate();
            return getMessageByID(message_id);

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * insert a new message object to the database
     * the message constraint is that it can't be empty and it can't be longer than 254
     * @param Message message
     * @return the message that was added to the database
     */

    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here. You should only be inserting with the name column, so that the database may
            //automatically generate a primary key.
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // makes sure message isnt blank or too big
                if (message.getMessage_text() != "" && message.getMessage_text().length() < 254) {
                    ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                    if (pkeyResultSet.next()) {
                        int generated_message_id = pkeyResultSet.getInt(1);
                        return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                    }
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * get message based on message_id
     * @param int message_id
     * @return the message that was matched with the message id else null
     */

    public Message getMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();      
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                        );
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * get message based on posted_by
     * @param int posted_by
     * @return list of all messages that match the posted_by, else an empty list
     */

    public List<Message> getMessagesGivenPostedBy(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();      
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, posted_by);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                        );
                messages.add(message);
            }
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    /**
     * check if the message exists in database based on message_id
     * @param int message_id
     * @return the message that was matched with the message id else null
     */

    public boolean messageExists (int message_id) {
        Connection connection = ConnectionUtil.getConnection();      
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // Message message = new Message (
                // rs.getInt("message_id"),
                // rs.getInt("posted_by"),
                // rs.getString("message_text"),
                // rs.getLong("time_posted_epoch"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}

