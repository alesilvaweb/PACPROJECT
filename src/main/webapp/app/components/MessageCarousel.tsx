import React, { useState } from 'react';
import { Carousel, CarouselItem, CarouselControl, CarouselIndicators, CarouselCaption } from 'reactstrap';

interface Message {
  header: string;
  text: string;
}

const messages: Message[] = [
  {
    header: 'Mensagem 1',
    text: 'Conteúdo da Mensagem 1',
  },
  {
    header: 'Mensagem 2',
    text: 'Conteúdo da Mensagem 2',
  },
  {
    header: 'Mensagem 3',
    text: 'Conteúdo da Mensagem 3',
  },
  // Adicione mais mensagens conforme necessário
];

const MessageCarousel: React.FC = () => {
  const [activeIndex, setActiveIndex] = useState(0);
  const [animating, setAnimating] = useState(false);

  const next = () => {
    if (animating) return;
    const nextIndex = activeIndex === messages.length - 1 ? 0 : activeIndex + 1;
    setActiveIndex(nextIndex);
  };

  const previous = () => {
    if (animating) return;
    const nextIndex = activeIndex === 0 ? messages.length - 1 : activeIndex - 1;
    setActiveIndex(nextIndex);
  };

  const goToIndex = (newIndex: number) => {
    if (animating) return;
    setActiveIndex(newIndex);
  };

  const slides = messages.map((message, index) => (
    <CarouselItem onExiting={() => setAnimating(true)} onExited={() => setAnimating(false)} key={index}>
      <CarouselCaption captionHeader={message.header} captionText={message.text} />
    </CarouselItem>
  ));

  return (
    <Carousel activeIndex={activeIndex} next={next} previous={previous}>
      <CarouselIndicators items={messages} activeIndex={activeIndex} onClickHandler={goToIndex} />
      {slides}
      <CarouselControl direction="prev" directionText="Previous" onClickHandler={previous} />
      <CarouselControl direction="next" directionText="Next" onClickHandler={next} />
    </Carousel>
  );
};

export default MessageCarousel;
