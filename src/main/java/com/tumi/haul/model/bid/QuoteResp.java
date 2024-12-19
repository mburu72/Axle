package com.tumi.haul.model.bid;

public record QuoteResp(
        String id,
        String ownerDetails,
        String status,
        String amount,
        String availableDate,
        String imageUrl
) {
}
