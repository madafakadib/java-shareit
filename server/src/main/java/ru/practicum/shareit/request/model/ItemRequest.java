package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    @NotBlank(message = "Описание запроса обязательно")
    private String description;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @NotNull
    private LocalDateTime created;
}
