package input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Machine;

public class InputParser implements IParser<String, Machine> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Machine parse(String input) throws JsonProcessingException {
        InputWrapper inputWrapper = null;
        try {
            inputWrapper = mapper.readValue(input, InputWrapper.class);
        } catch (JsonProcessingException e) {
            System.err.println("Not able to parse input file");
            throw e;
        }
        return inputWrapper.getMachine();
    }
}
