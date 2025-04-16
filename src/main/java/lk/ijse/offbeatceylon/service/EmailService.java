package lk.ijse.offbeatceylon.service;


public interface EmailService {


    public String generateOTP();

    public void sendOtpEmail(String to);

    public void sendSuccessEmail(String to, String bookId);
}