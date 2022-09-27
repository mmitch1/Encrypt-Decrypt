package encryptor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
public class EncryptFilter implements Filter {

 private static final boolean debug = true;
 private FilterConfig filterConfig = null;


 public EncryptFilter() {
 }

 
 @Override
 public void init(FilterConfig filterConfig) {
  this.filterConfig = filterConfig;
  if (filterConfig != null) {
if (debug) {
 log("EncryptFilter:Initializing filter");
}
  }
 }


 @Override
 public void doFilter(ServletRequest request,
	  ServletResponse response, FilterChain chain)
throws IOException, ServletException {
  if (debug) {
log("EncryptFilter:doFilter()");
  }
  if (request instanceof HttpServletRequest) {
String strings =
	((HttpServletRequest) request).getParameter("strings");
log("Message: " + strings);
if (strings != null) {
 try {

  KeyGenerator keygenerator = KeyGenerator.getInstance("DESede");
 
  SecretKey secretkey = keygenerator.generateKey();
   
  Cipher cipher = Cipher.getInstance("DESede");
  cipher.init(Cipher.ENCRYPT_MODE, secretkey);
  byte[] encrypted = cipher.doFinal(strings.getBytes());
  cipher.init(Cipher.DECRYPT_MODE, secretkey);
  byte[] decrypted = cipher.doFinal(encrypted);


  request.setAttribute("encrypted", new String(encrypted));
  request.setAttribute("decrypted", new String(decrypted));
 
  request.getRequestDispatcher("response.jsp").forward(request, response);
 } catch (Exception ex) {
  Logger.getLogger(EncryptFilter.class.getName()).log(Level.SEVERE,
 "Exception Generate in data encrypt and decrypt", ex);
 }
} else {
 chain.doFilter(request, response);
}
  }
 }


 public FilterConfig getFilterConfig() {
  return (this.filterConfig);
 }


 public void setFilterConfig(FilterConfig filterConfig) {
  this.filterConfig = filterConfig;
 }

 
 @Override
 public void destroy() {
  filterConfig = null;
 }

 
 public void log(String msg) {
  filterConfig.getServletContext().log(msg);
 }
}