package com.restful.booker.modules;

import com.restful.booker.api.helper.RequestWrapper;
import com.restful.booker.api.endpoints.Endpoints;

import io.restassured.response.Response;
import static com.restful.booker.api.helper.MyConfig.getBundle;

public class Modules {
	RequestWrapper req;

	public Modules() {
		// TODO Auto-generated constructor stub
		req = RequestWrapper.getInstance();
	}

	public Response getBookingIds() {
		return req.getRequest(Endpoints.GETBOOKINGIDS, null);
	}

	public Response getBooking(int bookingId) {
		getBundle().put("BOOKING_ID", String.valueOf(bookingId));
		return req.getRequest(Endpoints.GETBOOKING, null);
	}

	public Response updateBooking(String firstname, String lastname, int price, boolean depositPaid, String checkinDate,
			String checkOutDate, String additional, int update_id) {
		getBundle().put("booking.update.firstname", firstname);
		getBundle().put("booking.update.lastname", lastname);
		getBundle().put("booking.update.price", String.valueOf(price));
		getBundle().put("booking.update.paid", String.valueOf(depositPaid));
		getBundle().put("booking.update.checkin", checkinDate);
		getBundle().put("booking.update.checkout", checkOutDate);
		getBundle().put("booking.update.additional", additional);
		getBundle().put("UPDATE_ID", String.valueOf(update_id));
		return req.putRequest(Endpoints.UPDATEBOOKING, null, getBundle().get("payload.booking.update"));
	}

	public Response createBooking(String firstname, String lastname, int price, boolean depositPaid, String checkinDate,
			String checkOutDate, String additional) {
		getBundle().put("booking.create.firstname", firstname);
		getBundle().put("booking.create.lastname", lastname);
		getBundle().put("booking.create.price", String.valueOf(price));
		getBundle().put("booking.create.paid", String.valueOf(depositPaid));
		getBundle().put("booking.create.checkin", checkinDate);
		getBundle().put("booking.create.checkout", checkOutDate);
		getBundle().put("booking.create.additional", additional);
		return req.postRequest(Endpoints.CREATEBOOKING, null, getBundle().get("payload.booking.create"));
	}

	public Response patchBooking(String firstname, String lastname, int patch_id) {
		getBundle().put("booking.patch.firstname", firstname);
		getBundle().put("booking.patch.lastname", lastname);
		getBundle().put("PATCH_ID", String.valueOf(patch_id));
		return req.patchRequest(Endpoints.PATCHBOOKING, null, getBundle().get("payload.booking.patch"));
	}

	public Response deleteBooking(int delete_id) {
		getBundle().put("DELETE_ID", String.valueOf(delete_id));
		return req.deleteRequest(Endpoints.DELETEBOOKING, null);
	}
}
