package dev.robertfuller.little_thing.dtos;

@lombok.Builder
@lombok.extern.jackson.Jacksonized
@lombok.Value
public class Response<T> {
    T data;

    public static <T> Response<T> of(T data) {
        return Response.<T>builder().data(data).build();
    }
}
