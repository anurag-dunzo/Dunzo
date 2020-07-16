package input;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IParser<T, U> {
    public U parse(T input) throws JsonProcessingException;
}
