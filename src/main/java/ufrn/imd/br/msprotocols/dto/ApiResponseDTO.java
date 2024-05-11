package ufrn.imd.br.msprotocols.dto;

public class ApiResponseDTO<DTO> {
    private boolean success;
    private String message;
    private DTO data;

    private DTO error;

    public ApiResponseDTO(boolean success, String message, DTO data, DTO error) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public ApiResponseDTO() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DTO getData() {
        return data;
    }

    public void setData(DTO data) {
        this.data = data;
    }

    public DTO getError() {
        return error;
    }

    public void setError(DTO error) {
        this.error = error;
    }
}
