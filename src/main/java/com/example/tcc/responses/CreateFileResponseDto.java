package com.example.tcc.responses;

public class CreateFileResponseDto {
    private Long id;
    private Long userId;

    public CreateFileResponseDto(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
