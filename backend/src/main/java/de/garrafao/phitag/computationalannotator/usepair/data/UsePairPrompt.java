package de.garrafao.phitag.computationalannotator.usepair.data;

import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsePairPrompt {
    public List<ChatMessage> getChatMessages(final String prompt, final String firstUsage,
                                             final String secondUsage, final String lemma) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage("system", "You are a highly trained text data annotation tool which can give subjective response.");
        messages.add(systemMessage);

        ChatMessage instructionMessage = new ChatMessage("user", prompt);
        messages.add(instructionMessage);

        ChatMessage firstUsageMessage = new ChatMessage("user", "Sentence 1: " + firstUsage);
        messages.add(firstUsageMessage);

        ChatMessage secondUsageMessage = new ChatMessage("user", "Sentence 2: " + secondUsage);
        messages.add(secondUsageMessage);

        ChatMessage targetWord = new ChatMessage("user", "Targetword: " + lemma);
        messages.add(targetWord);
        ChatMessage returnType = new ChatMessage("user", "give me '''Judgement''' in '''single integer''' " +
                "for example:if your Judgement is Identical, then give me 4 and if your judgement is not related then give 1");
        messages.add(returnType);

        return messages;
    }
}
