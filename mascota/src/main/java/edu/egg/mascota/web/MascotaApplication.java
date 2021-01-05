package edu.egg.mascota.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



@SpringBootApplication
public class MascotaApplication extends SpringBootServletInitializer {

	   @Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
			return application.sources(MascotaApplication.class);    
	   }
	   
	public static void main(String[] args)throws Exception  {
		SpringApplication.run(MascotaApplication.class, args);
                
                
	}
        
        


}
