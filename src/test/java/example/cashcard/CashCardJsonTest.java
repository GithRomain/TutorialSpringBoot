package example.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CashCardJsonTest {

    CashCard[] cashCards;

    @Autowired
    private JacksonTester<CashCard> json;
    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    @BeforeEach
    void setUp() {
        this.cashCards = Arrays.array(
                new CashCard(99L, 123.45, "sarah1"),
                new CashCard(100L, 1.00, "sarah1"),
                new CashCard(101L, 150.00, "sarah1"));
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45, "sarah1");
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(123.45);
        assertThat(json.write(cashCard)).hasJsonPathStringValue("@.owner");
        assertThat(json.write(cashCard)).extractingJsonPathStringValue("@.owner")
                .isEqualTo("sarah1");
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        String expected = """
                {
                    "id":1000,
                    "amount":67.89,
                    "owner":"sarah1"
                }
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new CashCard(1000L, 67.89, "sarah1"));
        assertThat(json.parseObject(expected).id()).isEqualTo(1000);
        assertThat(json.parseObject(expected).amount()).isEqualTo(67.89);
        assertThat(json.parseObject(expected).owner()).isEqualTo("sarah1");
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected = """
                [
                    { "id": 99, "amount": 123.45, "owner":"sarah1" },
                    { "id": 100, "amount": 1.00, "owner":"sarah1" },
                    { "id": 101, "amount": 150.00, "owner":"sarah1" }
                ]
                """;
        assertThat(jsonList.parse(expected))
                .isEqualTo(cashCards);
    }
}
