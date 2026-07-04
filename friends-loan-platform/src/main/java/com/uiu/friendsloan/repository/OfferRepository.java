package com.uiu.friendsloan.repository;

import com.uiu.friendsloan.entity.LoanPost;
import com.uiu.friendsloan.entity.Offer;
import com.uiu.friendsloan.entity.OfferStatus;
import com.uiu.friendsloan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByLoanPost(LoanPost loanPost);

    List<Offer> findByLoanPostAndStatus(LoanPost loanPost, OfferStatus status);

    List<Offer> findByLender(User lender);

    // ✅ এই মেথডটা যোগ করো
    List<Offer> findByLoanPostAndIdNot(LoanPost loanPost, Long offerId);

    boolean existsByLoanPostAndLenderAndStatus(LoanPost loanPost, User lender, OfferStatus status);
}