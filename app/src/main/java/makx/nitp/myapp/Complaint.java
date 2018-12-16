package makx.nitp.myapp;

public class Complaint {

    private String Message;

    public Complaint() {}  // Needed for Firebase

//    public Complaint(String message) {
//        this.Message = message;
//    }

    public String getMessage() { return Message; }

    public void setMessage(String message) { this.Message = message; }
}