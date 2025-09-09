package org.saadMeddiche.responses;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BookDetailsResponse  {

    public BookResponse book;
    public List<ChapterResponse> chapters;
    public List<PageResponse> pages;
    public List<ParagraphResponse> paragraphs;

}