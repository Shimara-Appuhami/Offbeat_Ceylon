package lk.ijse.offbeatceylon.service;

public interface OtpService {
    public void storeOtp(String email, String otp);
    public boolean verifyOtp(String email, String otp);
    public void resendOtp(String email);
}