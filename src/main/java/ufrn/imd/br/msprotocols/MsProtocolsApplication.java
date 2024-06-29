package ufrn.imd.br.msprotocols;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsProtocolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProtocolsApplication.class, args);
	}

}
