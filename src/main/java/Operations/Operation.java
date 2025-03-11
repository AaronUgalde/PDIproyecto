package Operations;

import Model.Image;

import Operations.Histogram.Histogram;
import Operations.RGB.RGB;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Brightness.class, name = "BRIGHTNESS"),
        @JsonSubTypes.Type(value = Contrast.class, name = "CONTRAST"),
        @JsonSubTypes.Type(value = Gray.class, name = "GRAY"),
        @JsonSubTypes.Type(value = RGB.class, name = "RGB"),
        @JsonSubTypes.Type(value = Histogram.class, name = "HISTOGRAM"),
        @JsonSubTypes.Type(value = OpenJFrame.class, name = "OPEN"),
        @JsonSubTypes.Type(value = Save.class, name = "SAVE")
})

public interface Operation {
    void apply(Image image);
}
