package Operations;

import Model.Image;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OperationProcessor {
    private final List<Operation> operations;

    public OperationProcessor(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.operations = mapper.readValue(jsonFile, new TypeReference<List<Operation>>(){});
    }

    public void applyOperations(Image image) {
        for (Operation operation : operations) {
            operation.apply(image);
        }
    }
}
