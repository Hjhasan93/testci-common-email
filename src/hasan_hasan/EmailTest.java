package hasan_hasan;

import static org.junit.Assert.*;

import java.util.Date;

import javax.mail.Session;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {

    // Email object used in each test
    private Email email;

    // Create a fresh email object before each test
    @Before
    public void setUp() {
        email = new SimpleEmail();
    }

    // Clean up after each test
    @After
    public void tearDown() {
        email = null;
    }

    // Test adding two valid BCC email addresses
    @Test
    public void testAddBcc_validEmails() throws EmailException {
        email.addBcc("bcc1@example.com");
        email.addBcc("bcc2@example.com");
        assertEquals(2, email.getBccAddresses().size());
    }

    // Test adding an invalid BCC email address
    @Test(expected = EmailException.class)
    public void testAddBcc_invalidEmail() throws EmailException {
        email.addBcc("invalid-email");
    }

    // Test adding a valid CC email address
    @Test
    public void testAddCc_validEmail() throws EmailException {
        email.addCc("cc@example.com");
        assertEquals(1, email.getCcAddresses().size());
    }

    // Test adding multiple CC email addresses
    @Test
    public void testAddCc_multipleEmails() throws EmailException {
        email.addCc("a@test.com");
        email.addCc("b@test.com");
        assertEquals(2, email.getCcAddresses().size());
    }

    // Test adding an invalid CC email address
    @Test(expected = EmailException.class)
    public void testAddCc_invalidEmail() throws EmailException {
        email.addCc("invalid-email");
    }

    // Test adding a valid header and checking it in the MIME message
    @Test
    public void testAddHeader_validHeader() throws Exception {
        email.addHeader("X-Test-Header", "HeaderValue");
        email.setHostName("smtp.example.com");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        ((SimpleEmail) email).setMsg("Test message body");

        email.buildMimeMessage();

        String[] headerValue = email.getMimeMessage().getHeader("X-Test-Header");
        assertNotNull(headerValue);
        assertEquals("HeaderValue", headerValue[0]);
    }

    // Test adding multiple headers
    @Test
    public void testAddHeader_multipleHeaders() throws Exception {
        email.addHeader("Header1", "Value1");
        email.addHeader("Header2", "Value2");

        email.setHostName("smtp.example.com");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test");
        ((SimpleEmail) email).setMsg("Body");

        email.buildMimeMessage();

        assertNotNull(email.getMimeMessage().getHeader("Header1"));
        assertNotNull(email.getMimeMessage().getHeader("Header2"));
    }

    // Test adding a header with an empty value should throw an exception
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeader_emptyValue() {
        email.addHeader("Test", "");
    }

    // Test adding a valid reply-to email and name
    @Test
    public void testAddReplyTo_validEmailAndName() throws EmailException {
        email.addReplyTo("reply@example.com", "Reply User");
        assertEquals(1, email.getReplyToAddresses().size());
        assertEquals("reply@example.com", email.getReplyToAddresses().get(0).getAddress());
    }

    // Test adding a reply-to email without a name
    @Test
    public void testAddReplyTo_noName() throws EmailException {
        email.addReplyTo("reply@test.com", null);
        assertEquals(1, email.getReplyToAddresses().size());
        assertEquals("reply@test.com", email.getReplyToAddresses().get(0).getAddress());
    }

    // Test adding an invalid reply-to email
    @Test(expected = EmailException.class)
    public void testAddReplyTo_invalidEmail() throws EmailException {
        email.addReplyTo("invalid-email", "Bad User");
    }

    // Test building the MIME message successfully
    @Test
    public void testBuildMimeMessage_success() throws Exception {
        email.setHostName("smtp.example.com");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        ((SimpleEmail) email).setMsg("Test message body");

        email.buildMimeMessage();

        assertNotNull(email.getMimeMessage());
    }

    // Test getting host name after it is set
    @Test
    public void testGetHostName_afterSetHostName() {
        email.setHostName("smtp.example.com");
        assertEquals("smtp.example.com", email.getHostName());
    }

    // Test default host name before setting it
    @Test
    public void testGetHostName_defaultNull() {
        assertNull(email.getHostName());
    }

    // Test getting the mail session after setting the host name
    @Test
    public void testGetMailSession_notNull() throws EmailException {
        email.setHostName("smtp.example.com");
        Session session = email.getMailSession();
        assertNotNull(session);
        assertEquals("smtp.example.com", session.getProperty("mail.smtp.host"));
    }

    // Test that getSentDate returns a non-null date
    @Test
    public void testGetSentDate_notNull() {
        Date sentDate = email.getSentDate();
        assertNotNull(sentDate);
    }

    // Test getting socket connection timeout after setting a custom value
    @Test
    public void testGetSocketConnectionTimeout_afterSetValue() {
        email.setSocketConnectionTimeout(5000);
        assertEquals(5000, email.getSocketConnectionTimeout());
    }

    // Test the default socket connection timeout value
    @Test
    public void testSetSocketConnectionTimeout_default() {
        int timeout = email.getSocketConnectionTimeout();
        assertTrue(timeout >= 0);
    }

    // Test setting socket connection timeout to zero
    @Test
    public void testSocketTimeout_zero() {
        email.setSocketConnectionTimeout(0);
        assertEquals(0, email.getSocketConnectionTimeout());
    }

    // Test setting a valid from email address
    @Test
    public void testSetFrom_validEmail() throws EmailException {
        email.setFrom("sender@example.com");
        assertEquals("sender@example.com", email.getFromAddress().getAddress());
    }

    // Test setting an invalid from email address
    @Test(expected = EmailException.class)
    public void testSetFrom_invalidEmail() throws EmailException {
        email.setFrom("invalid-email");
    }
}