package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {

    @Test
    public void purchaseTicketsTestForPositiveScenario() {
        //Multiple ticketTypeRequest object created for different age group of people
        TicketTypeRequest ticketTypeRequestOne = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,2);
        TicketTypeRequest ticketTypeRequestTwo = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,3);
        TicketTypeRequest ticketTypeRequestThree = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,5);

        TicketService ticketService = new TicketServiceImpl();
        ticketService.purchaseTickets(9786743L, ticketTypeRequestOne, ticketTypeRequestTwo, ticketTypeRequestThree);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTicketsTestForOnlyInfants() {
        // ticketTypeRequest object created for infants only
        TicketTypeRequest ticketTypeRequestOne = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,2);

        TicketService ticketService = new TicketServiceImpl();
        ticketService.purchaseTickets(9786743L, ticketTypeRequestOne);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTicketsTestForInfantsAndChildren() {
        // ticketTypeRequest object created for infants and children
        TicketTypeRequest ticketTypeRequestOne = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,2);
        TicketTypeRequest ticketTypeRequestTwo = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,3);

        TicketService ticketService = new TicketServiceImpl();
        ticketService.purchaseTickets(9786743L, ticketTypeRequestOne, ticketTypeRequestTwo);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void purchaseTicketsTestForMoreThanTwentyTickets() {
        // ticketTypeRequest object created for more than 20 tickets
        TicketTypeRequest ticketTypeRequestOne = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,2);
        TicketTypeRequest ticketTypeRequestTwo = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,3);
        TicketTypeRequest ticketTypeRequestThree = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,18);

        TicketService ticketService = new TicketServiceImpl();
        ticketService.purchaseTickets(9786743L, ticketTypeRequestOne, ticketTypeRequestTwo, ticketTypeRequestThree);
    }


}
