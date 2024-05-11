package ufrn.imd.br.msprotocols.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpRequest<T> {
    private final String url;
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final Object body;
    private final Class<T> responseType;

    public HttpRequest(String url, HttpMethod method, HttpHeaders headers, Object body, Class<T> responseType) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.responseType = responseType;
    }

    public T sendRequest() {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType);

        return response.getBody();
    }
}
