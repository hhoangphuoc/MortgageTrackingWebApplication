import Particle from "./Particle";

export default class Emitter{
    particles = [] as Particle[];
    maxCount = 20;
    interval = 0.02;
    t=0;
    constructor (readonly root: HTMLElement){

    }
    update(dt = 1/60){
        this.t += dt;
        if (this.t >= this.interval){
            this.t -= this.interval;
            if (this.particles.length < this.maxCount){
                this.addParticle();
            }
        }
        for (const particle of this.particles){
            particle.update(dt);
        }
    }
    addParticle(){
        const particle = new Particle();
        particle.random();
        this.particles.push(particle);
        this.root.append(particle.element);
    }
    removeParticle(){
        const particle = this.particles.shift();
        particle?.element?.remove?.();
    }
}
