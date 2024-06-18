package cinema;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class Controller implements ErrorController {

    private final int TOTAL_ROWS = 9, TOTAL_COLUMNS = 9;
    private final String SUPER_SECRET_PASSWORD = "super_secret";
    Room cinemaRoom = new Room(TOTAL_ROWS, TOTAL_COLUMNS);

    @GetMapping("/seats")
    public Room getAvailableSeats() {
        return cinemaRoom;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat seat) {
        return cinemaRoom.purchase(seat);
    }

    @PostMapping("/return")
    public ResponseEntity<?> refundTicket(@RequestBody Ticket ticket) {
        return cinemaRoom.refund(ticket);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam Map<String, String> params) {
        String password = params.get("password");

        if (SUPER_SECRET_PASSWORD.equals(password)) {
            int income = cinemaRoom.calculateIncome();
            int availableSeats = cinemaRoom.getAvailable_seats().size();
            int purchasedTickets = cinemaRoom.getPurchasedTickets().size();

            return new ResponseEntity<>(Map.of(
                    "income", income,
                    "available", availableSeats,
                    "purchased", purchasedTickets
            ), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }
    }

}
