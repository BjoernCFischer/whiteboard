package de.bjoernfischer.whiteboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhiteboardMessage {
    private String id;
    private String message;
    private Date timestamp;
}
