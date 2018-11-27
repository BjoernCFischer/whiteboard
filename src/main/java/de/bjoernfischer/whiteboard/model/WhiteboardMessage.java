package de.bjoernfischer.whiteboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhiteboardMessage {
    @Id
    private String id;

    @NotBlank
    private String message;

    @NotBlank
    private Date timestamp;
}
