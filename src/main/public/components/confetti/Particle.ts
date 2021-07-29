import Vec3 from "./Vec3";

export default class Particle {
    g = -10;
    t = 0;
    ramp = 0.1;
    visible = 2;
    element = document.createElement("div");
    constructor(
        public pos = new Vec3(),
        public vel = new Vec3(),
        public rot = new Vec3(),
        public rotVel = new Vec3(),
    ){
        this.element.style.width = "0.75rem";
        this.element.style.height = "1rem";
        this.element.style.position = "absolute";
        this.element.style.left = "0px";
        this.element.style.top = "0px";
    }
    static colors = ["red", "green", "blue", "purple"];
    random(){
        this.pos.random(6)
        this.vel.random(10, 1);
        this.vel.y = (this.vel.y - 0.5) * 10;
        this.rot.random();
        this.rotVel.random();
        this.element.style.backgroundColor = Particle.colors[0| Math.random()*Particle.colors.length];
        this.t = 0;
    }
    static max = 0;
    update(dt: number = 1/60){
        this.t += dt;
        this.vel.y -= Math.max(-50, this.g*dt);
        this.pos.addMult(this.vel, dt);
        this.rot.addMult(this.rotVel, dt);
        this.element.style.left = `${this.pos.x}rem`;
        this.element.style.top = `${this.pos.y}rem`;
        const o = this.t < this.ramp ? this.t*100/this.ramp 
            : (this.t < this.visible ? 100 : 100-(this.t-this.visible)*100/this.ramp );
        if (o > Particle.max){
            Particle.max = o;
            console.log(o);
        }
        this.element.style.opacity = `${o}%`;
    }
}
