//
//package edu.egg.mascota.web.servicio;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificacionServicio {
//     
//    @Autowired
//    private JavaMailSender mailSender;
//    
//    // METODO PARA ENVIAR LOS MAIL
//    
//    // MARCAMOS ESTE METODO CON ASINCRONO(EL HILO DE EJECUCION NO ESPERA A QUE SE TERMINE DE ENVIAR EL MENSAJE, LO EJECUTA EN UN HILO PARALELO)
//    @Async
//    public void enviar(String cuerpo, String titulo, String mail){
//        SimpleMailMessage mensaje = new SimpleMailMessage ();
//        mensaje.setTo(mail);// a quien va dirigido
//        mensaje.setFrom("noreply@mascota.mascota.com");// a quien sale el mensaje//noreply@tinder.mascota.com
//        mensaje.setSubject(titulo);// el asunto
//        mensaje.setText(cuerpo);// seteo el cuerpo del mensaje
//        
//        // USAMOS EL MAILSENDER PARA ENVIAR ESE MENSAJE
//        mailSender.send(mensaje);
//    }
//}
