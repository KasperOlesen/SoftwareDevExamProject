package Migration;

import java.util.UUID;

public interface IBookIdentifierProvider {
    UUID getNextIdentifier();
}