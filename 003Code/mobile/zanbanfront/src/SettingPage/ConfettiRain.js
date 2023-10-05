import Confetti from 'react-confetti';

function ConfettiRain() {
    return (
        <Confetti width={window.innerWidth} height={window.innerHeight} />
    );
}

export default ConfettiRain;