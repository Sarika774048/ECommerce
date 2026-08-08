package com.infinitycart.service.impl;

import com.infinitycart.config.JwtProvider;
import com.infinitycart.domain.AccountStatus;
import com.infinitycart.domain.USER_ROLE;
import com.infinitycart.model.Address;
import com.infinitycart.model.Seller;
import com.infinitycart.model.VerificationCode;
import com.infinitycart.repository.SellerRepository;
import com.infinitycart.repository.VerificationCodeRepository;
import com.infinitycart.service.AddressRepository;
import com.infinitycart.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = this.getSellerByEmail(email);
        return this.getSellerByEmail(email);
    }


    @Override
    public Seller createSeller(Seller seller) throws Exception {

        String email = seller.getEmail().trim().toLowerCase();

        Seller existingSeller = sellerRepository.findByEmail(email);

        if (existingSeller != null) {
            throw new Exception("Seller already exists");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setEmail(email);
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setPickupAddress(savedAddress);

        // Set the initial status if your Seller entity has this field
        newSeller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws Exception {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new Exception("User not found with the Id"));
        return seller;
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if(seller == null){
            throw new Exception("Seller not found");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSeller(AccountStatus status) {
            return sellerRepository.findAllByAccountStatus(status);
    }


    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {

        Seller existingSeller = sellerRepository.findById(id)
                .orElseThrow(() -> new Exception("Seller not found with id " + id));

        // Update Seller Name
        if (seller.getSellerName() != null && !seller.getSellerName().isBlank()) {
            existingSeller.setSellerName(seller.getSellerName());
        }

        // Update Email
        if (seller.getEmail() != null && !seller.getEmail().isBlank()) {

            String email = seller.getEmail().trim().toLowerCase();

            if (!email.equalsIgnoreCase(existingSeller.getEmail())) {

                Seller emailExists = sellerRepository.findByEmail(email);

                if (emailExists != null) {
                    throw new Exception("Email already exists");
                }

                existingSeller.setEmail(email);
            }
        }

        // Update Mobile
        if (seller.getMobile() != null && !seller.getMobile().isBlank()) {
            existingSeller.setMobile(seller.getMobile());
        }

        // Update GSTIN
        if (seller.getGSTIN() != null && !seller.getGSTIN().isBlank()) {
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        // Update Business Details
        if (seller.getBusinessDetails() != null) {
            existingSeller.setBusinessDetails(seller.getBusinessDetails());
        }

        // Update Bank Details
        if (seller.getBankDetails() != null) {
            existingSeller.setBankDetails(seller.getBankDetails());
        }

        // Update Password
        if (seller.getPassword() != null && !seller.getPassword().isBlank()) {
            existingSeller.setPassword(
                    passwordEncoder.encode(seller.getPassword())
            );
        }

        // Update Pickup Address
        if (seller.getPickupAddress() != null) {

            Address existingAddress = existingSeller.getPickupAddress();

            if (existingAddress == null) {
                existingAddress = new Address();
            }

            Address newAddress = seller.getPickupAddress();

            if (newAddress.getName() != null)
                existingAddress.setName(newAddress.getName());

            if (newAddress.getMobile() != null)
                existingAddress.setMobile(newAddress.getMobile());

            if (newAddress.getLocality() != null)
                existingAddress.setLocality(newAddress.getLocality());

            if (newAddress.getCity() != null)
                existingAddress.setCity(newAddress.getCity());

            if (newAddress.getState() != null)
                existingAddress.setState(newAddress.getState());

            if (newAddress.getPinCode() != null)
                existingAddress.setPinCode(newAddress.getPinCode());

            if (newAddress.getAddresses() != null)
                existingAddress.setAddresses(newAddress.getAddresses());

            Address savedAddress = addressRepository.save(existingAddress);

            existingSeller.setPickupAddress(savedAddress);
        }

        return sellerRepository.save(existingSeller);
    }


    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new Exception("User not found with the Id"));
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        VerificationCode verificationCode =
                verificationCodeRepository.findByEmail(email);

        if (verificationCode == null) {
            throw new Exception("OTP not found");
        }

        if (!verificationCode.getOtp().equals(otp)) {
            throw new Exception("Invalid OTP");
        }

        Seller seller = getSellerByEmail(email);

        seller.setAccountStatus(AccountStatus.ACTIVE);

        verificationCodeRepository.delete(verificationCode);

        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow( () -> new Exception("User Not Found!!"));
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
