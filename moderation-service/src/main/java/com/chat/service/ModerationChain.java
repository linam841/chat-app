package com.chat.service;

import org.springframework.stereotype.Service;




@Service
public class ModerationChain {


    RegexProfanityHandler regexProfanityHandlerHandler;


    public ModerationChain(RegexProfanityHandler regexProfanityHandlerHandler
//                           SpamFilter spamFilter,
//                           HateSpeechHandler hateFilter
                           )
    {
        // Build chain: regex → spam → hate → ...
        this.regexProfanityHandlerHandler = regexProfanityHandlerHandler;
//                .setNext(spamFilter)
//                .setNext(hateFilter);
    }

    public boolean moderate(String message) {
        // Always start at the head of the chain
        return regexProfanityHandlerHandler.handle(message);
    }
}