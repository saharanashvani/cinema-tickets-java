package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.constants.TicketBookingConstants;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.logging.Logger;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private static final Logger LOGGER = Logger.getLogger(TicketServiceImpl.class.getName());
    private TicketPaymentService ticketPaymentService ;
    private SeatReservationService seatReservationService;

    /**
     *
     * @param accountId
     * @param ticketTypeRequests
     * @throws InvalidPurchaseException
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        /*List<TicketTypeRequest> ticketTypeRequestList =  Arrays.stream(ticketTypeRequests)
                .filter(t-> !t.getTicketType().equals(TicketTypeRequest.Type.INFANT)).collect(Collectors.toList());*/

        int totalAmount =0;
        int totalNumberOfTickets =0;
        boolean adultTicketFlag = false;
        for (TicketTypeRequest ttr : ticketTypeRequests) {
            if (!ttr.getTicketType().equals(TicketTypeRequest.Type.INFANT)) {
                if (ttr.getTicketType().equals(TicketTypeRequest.Type.CHILD)) {
                    totalAmount = totalAmount + TicketBookingConstants.CHILD_TICKET_PRICE * ttr.getNoOfTickets();
                } else {
                    totalAmount = totalAmount + TicketBookingConstants.ADULT_TICKET_PRICE * ttr.getNoOfTickets();
                    adultTicketFlag = true;
                }
                totalNumberOfTickets = totalNumberOfTickets + ttr.getNoOfTickets();
            } else {
                //skip record
            }
        }
        submitRequest(accountId, totalAmount, totalNumberOfTickets, adultTicketFlag);
        // Note : test cases are written in uk.gov.dwp.uc.pairtest.TicketServiceImplTest
    }

    /**
     *
     * @param accountId
     * @param totalAmount
     * @param totalNumberOfTickets
     * @param adultTicketFlag
     */
    private void submitRequest(Long accountId, int totalAmount, int totalNumberOfTickets, boolean adultTicketFlag) {
        if (totalNumberOfTickets ==0){
            throw new InvalidPurchaseException(TicketBookingConstants.INVALID_REQUEST_MESSAGE);
        } else if (totalNumberOfTickets >20) {
            throw new InvalidPurchaseException(TicketBookingConstants.MORE_THAN_20_MESSAGE);
        } else if (!adultTicketFlag) {
            throw new InvalidPurchaseException(TicketBookingConstants.NOT_BOOKED_FOR_ADULT_MESSAGE);
        } else {
            ticketPaymentService = new TicketPaymentServiceImpl();
            seatReservationService = new SeatReservationServiceImpl();
            ticketPaymentService.makePayment(accountId, totalAmount);
            seatReservationService.reserveSeat(accountId, totalNumberOfTickets);
            LOGGER.info(totalNumberOfTickets + " Ticket booked successfully");
        }
    }
}
