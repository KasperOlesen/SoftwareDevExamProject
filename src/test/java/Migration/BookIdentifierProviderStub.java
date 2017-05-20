package Migration;

import java.util.UUID;

public class BookIdentifierProviderStub implements IBookIdentifierProvider {
    int numberOfCalls = -1;

    public UUID getNextIdentifier() {
        numberOfCalls += 1;

        switch (numberOfCalls) {
        case 0:
            return UUID.fromString("1cf23a40-4c3c-444c-9014-04eee2211f1a");

        case 1:
            return UUID.fromString("792f42e0-7ae4-44dd-a197-eeb4a66f8c42");
        }

        return null;
    }
}