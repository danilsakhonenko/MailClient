package pks.mailclient.sessions;

public class Pop3Session extends MailReadSession {

    public Pop3Session(String email, String pass, int port) throws Exception {
        super(email, pass, port);
        _host = "pop." + getHostPart();
        _properties.put("mail.pop3.ssl.enable", "true");
        _properties.put("mail.pop3.starttls.enable", "true");
        _properties.put("mail.pop3.connectiontimeout", "10000");
        _properties.put("mail.pop3.timeout", "10000");
        //properties.put("mail.pop3.starttls.required", "true");
        _session = getSession();
        _session.setDebug(true);
        checkConnection();
    }

    @Override
    protected void checkConnection() throws Exception {
        try {
            _store = _session.getStore("pop3");
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
