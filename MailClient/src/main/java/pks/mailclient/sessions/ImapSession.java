package pks.mailclient.sessions;

public class ImapSession extends MailReadSession{

    public ImapSession(String email, String pass, int port) throws Exception{
        super(email,pass,port);
        _host = "imap." + getHostPart();
        _properties.put("mail.pop3.ssl.enable", "true");
        _properties.put("mail.imap.starttls.enable", "true");
        _properties.put("mail.imap.connectiontimeout", "10000");
        _properties.put("mail.imap.timeout", "10000");
        //properties.put("mail.imap.starttls.required", "true");
        _session = getSession();
        _session.setDebug(true);
        checkConnection();
    }

    @Override
    protected void checkConnection() throws Exception {
        try {
            _store = _session.getStore("imap");
            _store.connect(_host, _userEmail, _password);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (_store != null) {
                _store.close();
            }
        }
    }
    
}
