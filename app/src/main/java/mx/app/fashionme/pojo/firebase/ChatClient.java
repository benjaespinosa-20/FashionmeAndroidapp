package mx.app.fashionme.pojo.firebase;

public class ChatClient {
    private String uid;
    private Message messages;
    private User users;

    public ChatClient(){}

    public ChatClient(String uid) {
        this.uid = uid;
    }

    public ChatClient(Message messages, User users) {
        this.messages = messages;
        this.users = users;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Message getMessages() {
        return messages;
    }

    public void setMessages(Message messages) {
        this.messages = messages;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "ChatClient{" +
                "uid='" + uid + '\'' +
                ", messages=" + messages +
                ", users=" + users +
                '}';
    }
}
