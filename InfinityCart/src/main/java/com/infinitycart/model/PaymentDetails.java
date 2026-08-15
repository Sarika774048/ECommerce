package com.infinitycart.model;

import com.infinitycart.domain.PaymentStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDetails {

    private String paymentId;

    private String razorPaymentLinkId;

    private String razorPaymentLinkReferenceId;

    private String razorPaymentLinkStatus;

    private String razorPaymentLinkZWSP;

    private PaymentStatus status;

}
